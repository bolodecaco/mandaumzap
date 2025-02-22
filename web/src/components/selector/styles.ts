import styled from 'styled-components'

export const Container = styled.div`
  position: relative;
`

export const SelectorButton = styled.button<{
  $width?: string
  $height?: string
}>`
  height: ${({ $height }) => $height ?? '2.5rem'};
  max-width: ${({ $width }) => $width ?? '100%'};
  background-color: ${({ theme }) => theme.colors.input};
  border: 1px solid ${({ theme }) => theme.colors.darkBorder};
  border-radius: 0.5rem;
  overflow: hidden;
  display: flex;
  padding: 0 0.75rem;
  gap: 0.25rem;
  display: flex;
  align-items: center;
  justify-content: first baseline;

  cursor: pointer;
  font-size: 0.875rem;
  font-weight: 400;
  letter-spacing: 0.05px;

  &:hover {
    background: ${({ theme }) => theme.colors.hover};
  }
`

export const SelectorMenu = styled.ul<{ open: boolean }>`
  position: absolute;
  top: 100%;
  left: 0;
  background: ${({ theme }) => theme.variants.button.primary.color};
  border: 1px solid ${({ theme }) => theme.colors.darkBorder};
  border-radius: 0.5rem;
  padding: 0.5rem 0;
  width: 9.375rem;
  display: ${({ open }) => (open ? 'block' : 'none')};
`

export const SelectorItem = styled.li`
  padding: 0.5rem 1rem;
  cursor: pointer;
  &:hover {
    background: ${({ theme }) => theme.colors.hover};
  }
`
