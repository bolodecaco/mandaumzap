'use server'

import { fetcher } from '@/lib/api'
import { createServerAction } from '../utils/createServerAction'
import { throwGenericError } from '../utils/throwGenericError'

export const clearHistory = createServerAction(async () => {
  try {
    const response = await fetcher('/user/messages', {
      method: 'DELETE',
    })

    return response
  } catch (err) {
    throwGenericError(err)
  }
})
