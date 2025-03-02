import { getAllChats, Sort } from '@/app/actions/chats/getAllChats'
import { useInfiniteQuery } from '@tanstack/react-query'

interface UseGetChatsParams {
  sessionId?: string
  sort?: Sort
  search?: string
  pageSize?: number
}

export const useGetChats = ({
  sessionId,
  sort,
  search,
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

      console.log('search', search)

      if (!response.success) {
        throw new Error(response.error)
      }

      return response.value
    },
    getNextPageParam: (lastPage) =>
      lastPage.last ? undefined : lastPage.number + 1,
    initialPageParam: 0,
  })
}
