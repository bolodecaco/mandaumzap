'use server'

import { auth, signOut } from '@/auth'

type NextFetchRequestConfig = {
  revalidate?: number | false
  tags?: string[]
}

interface FetchAPIOptions {
  method: 'GET' | 'POST' | 'PUT' | 'DELETE'
  authToken?: string
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  body?: any
  next?: NextFetchRequestConfig
}

export async function fetcher(endpoint: string, options: FetchAPIOptions) {
  const { method, body, next } = options

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

  try {
    const url = `${process.env.API_URL}${endpoint}`
    const response = await fetch(url, headers)

    if (response.status === 401) {
      await signOut()
    }

    const contentType = response.headers.get('content-type')
    if (contentType?.includes('application/json') && response.ok) {
      return await response.json()
    } else {
      return { status: response.status, statusText: response.statusText }
    }
  } catch (error) {
    console.error(`Error ${method} data:`, error)
    throw error
  }
}
