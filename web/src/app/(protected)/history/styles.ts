import { Button } from '@/components/button'
import { Main, Row } from '@/lib/styled/global'
import styled from 'styled-components'

export const StyledMain = styled(Main)`
  flex-direction: row;
`
export const Delete = styled(Button)`
  color: ${({ theme }) => theme.colors.tertiary};

  &:hover {
    background-color: ${({ theme }) => theme.colors.border};
  }
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

export const Header = styled.header`
  display: flex;
  align-items: center;
  gap: 1rem;
  padding-block: 0.5rem;
`

export const Text = styled.div`
  color: ${({ theme }) => theme.colors.title};
  font-size: 0.875rem;
  line-height: 1.25rem;
  padding-block: 1rem;
`

export const Line = styled(Row)`
  width: 100%;

  &:hover {
    background-color: ${({ theme }) => theme.colors.border};
  }
`

export const Headline = styled.p`
  font-size: 0.75rem;
  font-weight: bold;
  letter-spacing: 0.05px;
  line-height: 1rem;
  text-transform: uppercase;
  color: ${({ theme }) => theme.colors.secondary};
`
