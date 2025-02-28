'use server'

import { fetcher } from '@/lib/api'
import { createServerAction } from '../utils/createServerAction'
import { throwGenericError } from '../utils/throwGenericError'

export type Sort = 'whatsAppId' | '-whatsAppId' | 'chatName' | '-chatName'

export interface GetChatsParams {
  search?: string
  sessionId?: string
  page?: number
  pageSize?: number
  sort?: Sort
}

export const getAllChats = createServerAction(
  async ({
    sessionId,
    search = '',
    page = 0,
    pageSize = 10,
    sort = 'chatName',
  }: GetChatsParams) => {
    try {
      const response = await fetcher('/user/chats', {
        method: 'GET',
        queryParams: {
          search,
          sessionId,
          page,
          pageSize,
          sort,
        },
      })
      return response
    } catch (err) {
      throwGenericError(err)
    }
  },
)
