'use server'

import { fetcher } from '@/lib/api'
import { createServerAction } from '../utils/createServerAction'
import { throwGenericError } from '../utils/throwGenericError'

interface SendMessageParams {
  messageId: string
}

export const sendMessage = createServerAction(
  async ({ messageId }: SendMessageParams) => {
    try {
      const response = await fetcher(`/user/messages/${messageId}/send`, {
        method: 'POST',
      })

      return response
    } catch (err) {
      throwGenericError(err)
    }
  },
)
