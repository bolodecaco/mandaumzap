import { styled } from 'styled-components'

interface ContainerProps {
  $width?: string
  $height?: string
}

export const Container = styled.div<ContainerProps>`
  box-sizing: border-box;
  background-color: ${({ theme }) => theme.colors.input};
  border: 1px solid ${({ theme }) => theme.colors.border};
  border-radius: 0.5rem;
  overflow: hidden;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  height: ${({ $height }) => $height ?? '2.5rem'};
  max-width: ${({ $width }) => $width ?? '100%'};
  width: 100%;
  padding-inline: 0.75rem;

  &:focus-within {
    outline-width: 0.15rem;
    outline-color: ${({ theme }) => theme.colors.primaryForeground};
    outline-style: solid;
  }
`

export const StyledInput = styled.input`
  all: unset;
  box-sizing: border-box;
  background-color: transparent;
  width: 100%;
  max-width: 100%;
`

export const Keybind = styled.span`
  all: unset;
  font-size: 0.625rem;
  font-weight: 600;
  padding: 0.35rem;
  color: ${({ theme }) => theme.colors.title};
  background-color: ${({ theme }) => theme.colors.keybind};
  white-space: nowrap;
  border-radius: 0.25rem;
`
