import { Row, Title, Wrapper } from '@/lib/styled/global'
import { styled } from 'styled-components'

export const Background = styled.div`
  position: absolute;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.35);
  display: flex;
  justify-content: center;
  align-items: center;
`

export const StyledWrapper = styled(Wrapper)`
  height: fit-content;
`
export const Header = styled(Row)`
  justify-content: space-between;
  align-items: center;
`

export const StyledTitle = styled(Title)`
  font-size: 1.125rem;
  font-weight: 600;
`
