'use client'

import { getAllLists } from '@/app/actions/lists/getAllLists'
import { useQuery } from '@tanstack/react-query'

interface UseGetListsParams {
  search?: string
}

export const useGetLists = ({ search }: UseGetListsParams) => {
  const { data, error, isLoading, refetch } = useQuery({
    queryKey: ['lists', search],
    queryFn: () => getAllLists({ search }),
  })
  return {
    data: data?.success ? data.value : undefined,
    error: !data?.success ? data?.error : error,
    refetch,
    isLoading,
  }
}
