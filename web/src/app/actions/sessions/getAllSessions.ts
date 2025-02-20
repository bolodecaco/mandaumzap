'use server'

import { fetcher } from '@/lib/api'
import { createServerAction } from '../utils/createServerAction'
import { throwGenericError } from '../utils/throwGenericError'

export const getAllSesions = createServerAction(async () => {
  try {
    const response = await fetcher('/user/sessions', {
      method: 'GET',
    })
    return response
  } catch (err) {
    throwGenericError(err)
  }
})
