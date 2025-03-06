export interface ServerActionResponse<T> {
  success: boolean
  value: T
  error?: string
}

interface SpringSort {
  sorted: boolean
  unsorted: boolean
  empty: boolean
}

export interface SpringPagination<T> {
  content: T[]
  pageable: {
    pageNumber: number
    pageSize: number
    sort: SpringSort
  }
  totalElements: number
  totalPages: number
  last: boolean
  first: boolean
  size: number
  number: number
  sort: SpringSort
  numberOfElements: number
  empty: boolean
}
