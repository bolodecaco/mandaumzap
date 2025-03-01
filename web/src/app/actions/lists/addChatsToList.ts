'use server'

import { fetcher } from '@/lib/api'
import { createServerAction } from '../utils/createServerAction'
import { throwGenericError } from '../utils/throwGenericError'

interface AddChatsToListParams {
  chats: { chatId: string }[]
  listId: string
}

export const addChatsToList = createServerAction(
  async ({ chats, listId }: AddChatsToListParams) => {
    try {
      const response = await fetcher(`/user/lists/${listId}/chats`, {
        method: 'POST',
        body: chats,
      })

      return response
    } catch (err) {
      throwGenericError(err)
    }
  },
)
