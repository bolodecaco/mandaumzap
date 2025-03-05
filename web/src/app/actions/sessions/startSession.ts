'use server'

import { fetcher } from '@/lib/api'
import { createServerAction } from '../utils/createServerAction'
import { throwGenericError } from '../utils/throwGenericError'

interface StartSessionParams {
  id: string
}

export const startSession = createServerAction(
  async ({ id }: StartSessionParams) => {
    try {
      const response = await fetcher(`/user/sessions/${id}/start`, {
        method: 'PATCH',
      })
      return response
    } catch (err: unknown) {
      throwGenericError(err)
    }
  },
)
