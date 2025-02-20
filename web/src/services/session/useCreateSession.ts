import { createSession } from '@/app/actions/sessions/createSession'
import { useQuery } from '@tanstack/react-query'

export const useCreateSession = () => {
  const { data, error, isLoading, isRefetching, refetch } = useQuery({
    queryKey: ['session'],
    queryFn: () => createSession(),
  })
  return {
    data: data?.success ? data.value : undefined,
    error: !data?.success ? data?.error : error,
    refetch,
    isLoading: isLoading || isRefetching,
  }
}
