'use client'

import { getAllLists } from '@/app/actions/lists/getAllLists'
import { useQuery } from '@tanstack/react-query'

interface UseGetListsParams {
  search?: string
  sort?: 'title' | '-title' | 'lastActiveAt' | ''
}

export const useGetLists = ({ search, sort }: UseGetListsParams) => {
  const { data, error, isLoading, refetch } = useQuery({
    queryKey: ['lists', search, sort],
    queryFn: () => getAllLists({ search, sort }),
  })
  return {
    data: data?.success ? data.value : undefined,
    error: !data?.success ? data?.error : error,
    refetch,
    isLoading,
  }
}
