import { Session } from '@/@types/session'
import { getAllChats, GetChatsParams } from '@/app/actions/chats/getAllChats'
import { useQuery } from '@tanstack/react-query'

export const useGetChats = ({
  page,
  pageSize,
  search,
  sessionId,
  sort,
}: GetChatsParams) => {
  const { data, error, isLoading, refetch } = useQuery({
    queryKey: ['chats', page, pageSize, search, sessionId, sort],
    queryFn: () =>
      getAllChats({
        page,
        pageSize,
        search,
        sessionId,
        sort,
      }),
  })
  return {
    data: data?.success ? (data.value as Session[]) : undefined,
    error: !data?.success ? data?.error : error,
    refetch,
    isLoading,
  }
}
