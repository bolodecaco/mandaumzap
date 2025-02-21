import { Button } from '@/components/button'
import { Page } from '@/lib/styled/global'
import { styled } from 'styled-components'

export const StyledPage = styled(Page)`
  gap: 1rem;
`
export const Clear = styled(Button)`
  color: ${({ theme }) => theme.colors.tertiary};

  &:hover {
    background-color: ${({ theme }) => theme.colors.tertiary};
    color: white;
  }
`

export const AddList = styled(Button)`
  border: 1px solid ${({ theme }) => theme.colors.darkBorder};
  width: 2.5rem;
  height: 2.5rem;
  padding: 0;

  &:hover {
    background-color: ${({ theme }) => theme.colors.toolbar};
  }
`
