'use server'

import { fetcher } from '@/lib/api'
import { createServerAction } from '../utils/createServerAction'
import { throwGenericError } from '../utils/throwGenericError'

export const deleteAllSessions = createServerAction(async () => {
  try {
    const response = await fetcher('/user/sessions/', {
      method: 'DELETE',
    })
    return response
  } catch (err: unknown) {
    throwGenericError(err)
  }
})
