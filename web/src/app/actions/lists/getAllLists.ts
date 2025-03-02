'use server'

import { fetcher } from '@/lib/api'
import { createServerAction } from '../utils/createServerAction'
import { throwGenericError } from '../utils/throwGenericError'

interface GetAllListsParams {
  search?: string
}

export const getAllLists = createServerAction(
  async ({ search = '' }: GetAllListsParams) => {
    try {
      const response = await fetcher('/user/lists', {
        method: 'GET',
        queryParams: {
          search,
        },
      })
      return response
    } catch (err) {
      throwGenericError(err)
    }
  },
)
