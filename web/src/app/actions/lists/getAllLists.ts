'use server'

import { fetcher } from '@/lib/api'
import { createServerAction } from '../utils/createServerAction'
import { throwGenericError } from '../utils/throwGenericError'

interface GetAllListsParams {
  search?: string
  sort?: 'title' | '-title' | 'lastActiveAt' | ''
}

export const getAllLists = createServerAction(
  async ({ search = '', sort }: GetAllListsParams) => {
    try {
      const response = await fetcher('/user/lists', {
        method: 'GET',
        queryParams: {
          search,
          sort,
        },
      })
      return response
    } catch (err) {
      throwGenericError(err)
    }
  },
)
