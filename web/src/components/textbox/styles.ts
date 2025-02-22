import { styled } from 'styled-components'

export const Container = styled.div`
  box-sizing: border-box;
  width: 100%;
  height: 100%;
  border-radius: 0.75rem;
  overflow: hidden;
  border: 1px solid ${({ theme }) => theme.colors.border};
  background-color: ${({ theme }) => theme.colors.input};

  display: flex;
  flex-direction: column;

  &:focus-within {
    outline-width: 0.15rem;
    outline-color: ${({ theme }) => theme.colors.primaryForeground};
    outline-style: solid;
  }
`

export const TextArea = styled.textarea`
  box-sizing: border-box;
  font-family: 'Inter', sans-serif;
  background-color: ${({ theme }) => theme.colors.input};
  font-size: 1rem;
  padding: 0.75rem;
  border-radius: 0.5rem;
  resize: none;
  width: 100%;
  height: 100%;
  border: none;

  &:focus {
    outline: none;
  }
`
