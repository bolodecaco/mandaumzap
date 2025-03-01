'use client'

import { List } from '@/@types/list'
import { getAllLists } from '@/app/actions/lists/getAllLists'
import { useQuery } from '@tanstack/react-query'

export const useGetLists = () => {
  const { data, error, isLoading, refetch } = useQuery({
    queryKey: ['lists'],
    queryFn: () => getAllLists(),
  })
  return {
    data: data?.success ? (data.value as List[]) : undefined,
    error: !data?.success ? data?.error : error,
    refetch,
    isLoading,
  }
}
