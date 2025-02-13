import { Button } from '@/components/button'
import { Main } from '@/lib/styled/global'
import styled from 'styled-components'

export const StyledMain = styled(Main)`
  flex-direction: row;
`
export const Delete = styled(Button)`
  color: ${({ theme }) => theme.colors.tertiary};
`
