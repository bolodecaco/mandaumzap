import { styled } from 'styled-components'

export const Wrapper = styled.header`
  box-sizing: border-box;
  background-color: white;
  border-radius: 0.75rem;
  border: 1px solid ${({ theme }) => theme.colors.border};
  padding: 0 1.5rem;
  width: 100%;
  min-height: 4rem;
  height: 4rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
`

export const Title = styled.h3`
  color: ${({ theme }) => theme.colors.title};
  font-size: 1rem;
  font-weight: 600;
`
