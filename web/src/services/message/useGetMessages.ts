'use client'

import { getAllMessages } from '@/app/actions/messages/getAllMessages'
import { useQuery } from '@tanstack/react-query'

export const useGetMessages = ({ enabled }: { enabled?: boolean }) => {
  const { data, error, isLoading, refetch } = useQuery({
    queryKey: ['messages'],
    queryFn: () => getAllMessages(),
    enabled,
  })
  return {
    data: data?.success ? data.value : undefined,
    error: !data?.success ? data?.error : error,
    refetch,
    isLoading,
  }
}
