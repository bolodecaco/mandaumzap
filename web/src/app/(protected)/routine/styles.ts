import { Button } from '@/components/button'
import { Main } from '@/lib/styled/global'
import styled from 'styled-components'

export const StyledMain = styled(Main)`
  flex-direction: row;
`
export const Delete = styled(Button)`
  color: ${({ theme }) => theme.colors.tertiary};
`

export const List = styled.div`
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  height: 100%;
  width: 100%;
  max-height: 100%;
  overflow: hidden;
  overflow-y: auto;
  margin-left: -0.75rem;
`

export const DivInfo = styled.div`
  display: flex;
  align-items: center;
  gap: 0.5rem;
`
export const Text = styled.span`
  font-weight: 600;
  font-size: 0.875rem;
  color: ${({ theme }) => theme.colors.title};
`
