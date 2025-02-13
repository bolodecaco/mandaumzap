import { fetcher } from '@/lib/api'
import { useQuery } from '@tanstack/react-query'

export const useSessions = () => {
  const { data, error, isLoading } = useQuery({
    queryKey: ['sessions'],
    queryFn: () => fetcher('/user/sessions', { method: 'GET' }),
  })
  return {
    data,
    error,
    isLoading,
  }
}
