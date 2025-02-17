import { fetcher } from '@/lib/api'
import { useQuery } from '@tanstack/react-query'

export const useGetSessions = () => {
  const { data, error, isLoading, refetch } = useQuery({
    queryKey: ['sessions'],
    queryFn: () => fetcher('/user/sessions', { method: 'GET' }),
  })
  return {
    data,
    error,
    refetch,
    isLoading,
  }
}
