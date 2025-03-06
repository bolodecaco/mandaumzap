import { Session } from '@/@types/session'
import { getAllSesions } from '@/app/actions/sessions/getAllSessions'
import { useQuery } from '@tanstack/react-query'

export const useGetSessions = () => {
  const { data, error, isLoading, refetch } = useQuery({
    queryKey: ['sessions'],
    queryFn: () => getAllSesions(),
  })
  return {
    data: data?.success ? (data.value as Session[]) : undefined,
    error: !data?.success ? data?.error : error,
    refetch,
    isLoading,
  }
}
