'use server'

import { fetcher } from '@/lib/api'
import { throwGenericError } from '../utils/throwGenericError'
import { createServerAction } from '../utils/createServerAction'

export const getAllMessages = createServerAction(async () => {
  try {
    const response = await fetcher('/user/messages', {
      method: 'GET',
    })

    return response
  } catch (err) {
    throwGenericError(err)
  }
})
