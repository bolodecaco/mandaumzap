'use server'

import { fetcher } from '@/lib/api'
import { createServerAction } from '../utils/createServerAction'
import { throwGenericError } from '../utils/throwGenericError'

interface CreateListParams {
  listName: string
}

export const createList = createServerAction(
  async ({ listName }: CreateListParams) => {
    try {
      const response = await fetcher('/user/lists', {
        method: 'POST',
        body: {
          title: listName,
        },
      })

      return response
    } catch (err) {
      throwGenericError(err)
    }
  },
)
