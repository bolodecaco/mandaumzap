'use server'

import { fetcher } from '@/lib/api'
import { throwGenericError } from '../utils/throwGenericError'
import { createServerAction } from '../utils/createServerAction'

interface ReadNotificationParams {
  id: string
}

export const readNotifications = createServerAction(
  async ({ id }: ReadNotificationParams) => {
    try {
      const response = await fetcher(`/user/notifications/${id}`, {
        method: 'PATCH',
        body: {
          read: true,
        },
      })

      return response
    } catch (err) {
      throwGenericError(err)
    }
  },
)
