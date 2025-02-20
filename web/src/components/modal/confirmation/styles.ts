import { Button } from '@/components/button'
import styled from 'styled-components'

export const Description = styled.div`
  font-size: 1rem;
  line-height: 1.25rem;
  color: ${({ theme }) => theme.colors.title};
`

export const Cancel = styled(Button)`
  color: ${({ theme }) => theme.colors.tertiary};
  border: 1px solid ${({ theme }) => theme.colors.tertiary};

  &:hover {
    background-color: ${({ theme }) => theme.colors.tertiary};
    color: ${({ theme }) => theme.colors.background};
  }
`
