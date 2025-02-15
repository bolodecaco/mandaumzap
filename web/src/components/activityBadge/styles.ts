import { styled } from 'styled-components'

export const Container = styled.span<{ $active: boolean }>`
  display: flex;
  align-items: center;
  width: fit-content;
  height: 1.125rem;
  padding-inline: 0.5rem;
  background-color: transparent;
  border: 1px solid ${({ $active }) => ($active ? '#00B51B' : '#CF2B2B')};
  color: ${({ $active }) => ($active ? '#00B51B' : '#CF2B2B')};
  border-radius: 1rem;
  font-size: 0.75rem;
  font-weight: 500;
  gap: 0.25rem;
`
