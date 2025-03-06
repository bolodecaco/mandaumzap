'use server'

import { fetcher } from '@/lib/api'
import { throwGenericError } from '../utils/throwGenericError'
import { createServerAction } from '../utils/createServerAction'

export const deleteSession = createServerAction(async (sessionId: string) => {
  try {
    const response = await fetcher(`/user/sessions/${sessionId}`, {
      method: 'DELETE',
    })
    return response
  } catch (err: unknown) {
    throwGenericError(err)
  }
})
