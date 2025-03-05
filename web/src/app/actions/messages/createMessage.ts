'use server'

import { fetcher } from '@/lib/api'
import { createServerAction } from '../utils/createServerAction'
import { throwGenericError } from '../utils/throwGenericError'

interface CreateMessageParams {
  listId: string
  content: string
  url?: string
  sessionId: string
}

export const createMessage = createServerAction(
  async ({ content, listId, sessionId, url }: CreateMessageParams) => {
    try {
      const response = await fetcher('/user/messages', {
        method: 'POST',
        body: {
          broadcastListId: listId,
          content,
          url,
          sessionId,
        },
      })
      console.log(response)

      return response
    } catch (err) {
      throwGenericError(err)
    }
  },
)
