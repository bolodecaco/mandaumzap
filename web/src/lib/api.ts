'use server'

import { auth, signOut } from '@/auth'

type NextFetchRequestConfig = {
  revalidate?: number | false
  tags?: string[]
}

interface FetchAPIOptions {
  method: 'GET' | 'POST' | 'PUT' | 'DELETE'
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  body?: any
  next?: NextFetchRequestConfig
  queryParams?: Record<string, string | number | boolean | undefined>
}

export async function fetcher(endpoint: string, options: FetchAPIOptions) {
  const { method, body, next, queryParams } = options

  const session = await auth()
  const authToken = session?.accessToken

  const headers: RequestInit & { next?: NextFetchRequestConfig } = {
    method,
    headers: {
      'Content-Type': 'application/json',
      ...(authToken && { Authorization: `Bearer ${authToken}` }),
    },
    ...(body && { body: JSON.stringify(body) }),
    ...(next && { next }),
  }

  const queryString = queryParams
    ? `?${new URLSearchParams(
        Object.entries(queryParams).reduce(
          (acc, [key, value]) => {
            if (value !== undefined) {
              acc[key] = String(value)
            }
            return acc
          },
          {} as Record<string, string>,
        ),
      ).toString()}`
    : ''

  const url = `${process.env.API_URL}${endpoint}${queryString}`
  try {
    const response = await fetch(url, headers)

    if (response.status === 401) {
      await signOut()
    }

    if (!response.ok) {
      const error = await response.json().catch(() => response.text())
      const errorMessage = error?.message || 'Erro desconhecido'
      return Promise.reject(new Error(errorMessage))
    }

    const contentType = response.headers.get('Content-Type')
    const contentLength = response.headers.get('Content-Length')

    if (!contentType || contentLength === '0') {
      return null
    }

    const data = await response.json().catch(() => null)
    return data ?? null
  } catch (error) {
    return Promise.reject(
      new Error(
        `Error while trying to fetch data from ${url.toString()}: ${error}`,
      ),
    )
  }
}
