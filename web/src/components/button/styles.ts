import styled, { css } from 'styled-components'

type ButtonProps = {
  $variant?: 'primary' | 'ghost'
}

export const Container = styled.button<ButtonProps>`
  border-radius: 0.5rem;
  height: 2.5rem;
  padding: 0 1rem;
  gap: 0.25rem;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 0.875rem;
  font-weight: 600;
  letter-spacing: 0.05px;
  transition: 100ms linear;

  ${({ theme, $variant = 'primary' }) =>
    $variant === 'primary'
      ? css`
          background-color: ${theme.variants.button.primary.backgroundColor};
          border: 1px solid ${theme.variants.button.primary.borderColor};
          color: ${theme.variants.button.primary.color};

          &:hover {
            background-color: ${theme.variants.button.primary.hover
              .backgroundColor};
          }

          &:active {
            background-color: ${theme.variants.button.primary.active
              .backgroundColor};
          }
        `
      : css`
          background-color: transparent;
          border: 1px solid transparent;
        `};
`
