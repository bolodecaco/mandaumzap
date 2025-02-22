import { styled } from 'styled-components'

export const Container = styled.div`
  box-sizing: border-box;
  padding: 0.5rem 0.75rem;
  border-top: 1px solid ${({ theme }) => theme.colors.border};

  display: flex;
  align-items: center;
  gap: 0.5rem;
`

export const ButtonGroup = styled.div`
  box-sizing: border-box;
  display: flex;
  align-items: center;
  gap: 0.5rem;
`

export const Divider = styled.span`
  width: 1px;
  height: 100%;
  background-color: ${({ theme }) => theme.colors.border};
`

export const Wrapper = styled.button`
  all: unset;
  height: 2rem;
  width: 2rem;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 0.25rem;

  &:hover {
    background-color: ${({ theme }) => theme.colors.toolbar};
  }
`
