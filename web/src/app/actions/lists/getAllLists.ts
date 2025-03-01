'use server'

import { fetcher } from '@/lib/api'
import { createServerAction } from '../utils/createServerAction'
import { throwGenericError } from '../utils/throwGenericError'

export const getAllLists = createServerAction(async () => {
  try {
    const response = await fetcher('/user/lists', {
      method: 'GET',
    })
    console.log(response)
    return response
  } catch (err) {
    throwGenericError(err)
  }
})
