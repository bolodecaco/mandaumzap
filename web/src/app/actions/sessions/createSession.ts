'use server'

import { fetcher } from '@/lib/api'
import {
  createServerAction,
  ServerActionError,
} from '../utils/createServerAction'
import { Session } from '@/@types/session'

export const createSession = createServerAction<Session>(async () => {
  try {
    return fetcher('/user/sessions', {
      method: 'POST',
      body: {
        active: true,
      },
    })
  } catch (err: unknown) {
    if (err instanceof Error) {
      throw new ServerActionError(err.message)
    }
    throw new ServerActionError('Um erro desconhecido aconteceu')
  }
})
