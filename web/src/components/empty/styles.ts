import styled from 'styled-components'

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 0.625rem;
  height: 100%;
  width: 100%;
`
export const Message = styled.p`
  text-align: center;
  font-size: 1rem;
  font-weight: 500;
  line-height: 1.5rem;
  color: #878787;
`

export const Action = styled(Message)`
  text-decoration: underline;
  color: ${({ theme }) => theme.colors.primary};
  font-weight: 600;
  cursor: pointer;
`
