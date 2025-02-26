import { Content, Item, Trigger } from '@radix-ui/react-select'
import styled from 'styled-components'

interface ContainerProps {
  $width?: string
  $height?: string
}

export const Container = styled(Trigger)<ContainerProps>`
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 0.5rem;

  width: ${({ $width }) => $width ?? 'auto'};
  height: ${({ $height }) => $height ?? '2.5rem'};
  padding: 0 0.75rem;

  max-width: ${({ $width }) => $width ?? '100%'};
  background-color: ${({ theme }) => theme.colors.input};
  border: 1px solid ${({ theme }) => theme.colors.darkBorder};
  color: ${({ theme }) => theme.colors.title};
  border-radius: 0.5rem;

  cursor: pointer;
  font-size: 0.875rem;
  font-weight: 600;
  letter-spacing: 0.05px;

  &:hover {
    background: ${({ theme }) => theme.colors.hover};
  }
`

export const Popper = styled(Content)`
  box-sizing: border-box;
  width: 100%;
  background-color: ${({ theme }) => theme.colors.input};
  color: 'black';
  border-radius: 0.25rem;
  box-shadow: 0 0 0.25rem rgba(0, 0, 0, 0.5);
`

export const StyledItem = styled(Item)`
  padding: 0.75rem;
`

export const Value = styled.span<{ $placeholder?: boolean }>`
  font-weight: ${({ $placeholder }) => ($placeholder ? 400 : 600)};
  color: ${({ $placeholder, theme }) =>
    $placeholder ? 'black' : theme.colors.title};
`
