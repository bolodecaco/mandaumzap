import { Button } from '@/components/button'
import { Main } from '@/lib/styled/global'
import styled from 'styled-components'

export const StyledMain = styled(Main)`
  flex-direction: row;
`
export const Delete = styled(Button)`
  color: ${({ theme }) => theme.colors.tertiary};
`

export const ListHeader = styled.div`
  width: 100%;
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 1rem;
  border-bottom: 1px solid #e5e7eb;
`

export const Phone = styled.div`
  display: flex;
  align-items: center;
  justify-content: start;
  padding: 0.5rem 0;
  text-align: left;
  width: 12rem;
`
export const ListName = styled.div`
  display: flex;
  align-items: center;
  justify-content: start;
  padding: 0.5rem 0;
  text-align: left;
  max-width: 100%;
`

export const UserDiv = styled.div`
  display: flex;
  width: 19.125rem;
  flex-direction: row;
  align-items: center;
`

export const Th = styled.th`
  text-align: left;
  font-weight: bold;
  font-size: 0.75rem;
  padding: 0.5rem 0;
  color: ${({ theme }) => theme.colors.secondary};
`
