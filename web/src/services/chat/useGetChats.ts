import { getAllChats, Sort } from '@/app/actions/chats/getAllChats'
import { useInfiniteQuery } from '@tanstack/react-query'

interface UseGetChatsParams {
  sessionId?: string
  sort?: Sort
  search?: string
  pageSize?: number
  enabled?: boolean
}

export const useGetChats = ({
  sessionId,
  sort,
  search,
  enabled,
  pageSize = 10,
}: UseGetChatsParams) => {
  return useInfiniteQuery({
    queryKey: ['chats', sessionId, sort, search],
    queryFn: async ({ pageParam = 0 }) => {
      const response = await getAllChats({
        sessionId,
        sort,
        search,
        page: pageParam,
        pageSize,
      })

      if (!response.success) {
        throw new Error(response.error)
      }

      return response.value
    },
    getNextPageParam: (lastPage) =>
      lastPage.last ? undefined : lastPage.number + 1,
    initialPageParam: 0,
    enabled,
  })
}
