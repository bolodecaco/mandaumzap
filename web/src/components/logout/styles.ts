import { styled } from 'styled-components'
import { Button } from '../button'

export const Container = styled(Button)`
  padding: 0;
  width: 2rem;
  height: 2rem;

  &:hover {
    background-color: ${({ theme }) => theme.colors.toolbar};
  }
`
