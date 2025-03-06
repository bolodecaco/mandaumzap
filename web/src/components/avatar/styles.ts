import { styled } from 'styled-components'

export const Wrapper = styled.div`
  position: relative;
  width: 2rem;
  height: 2rem;
  overflow: hidden;
  border: 1px solid ${({ theme }) => theme.colors.border};
  border-radius: 9999px;
`
