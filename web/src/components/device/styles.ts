import styled from 'styled-components'
import { Button } from '../button'

export const Phone = styled.p`
  color: ${({ theme }) => theme.colors.title};
  font-size: 1rem;
  font-weight: 500;
`

export const Uptime = styled.p`
  color: #666;
  font-size: 0.75rem;
  letter-spacing: 0.05px;
  font-weight: 500;
`

export const Delete = styled(Button)`
  border-radius: 9999px;
  width: 2.5rem;
  height: 2.5rem;
  padding: 0;
  &:hover {
    background-color: #00000010;
  }
`
