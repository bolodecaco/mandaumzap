'use server'

import { fetcher } from '@/lib/api'
import { createServerAction } from '../utils/createServerAction'
import { Session } from '@/@types/session'
import { throwGenericError } from '../utils/throwGenericError'

export const createSession = createServerAction<Session>(async () => {
  try {
    const response = await fetcher('/user/sessions', {
      method: 'POST',
    })
    return response
  } catch (err: unknown) {
    throwGenericError(err)
  }
})
