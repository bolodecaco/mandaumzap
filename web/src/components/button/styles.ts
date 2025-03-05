import styled, { css, keyframes } from 'styled-components'

type ButtonProps = {
  $variant?: 'primary' | 'ghost'
  $weight?: 'bold' | 'normal'
}

const spin = keyframes`
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
`

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
  font-weight: ${({ $weight }) => ($weight === 'bold' ? 600 : 400)};
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

          &:disabled {
            background-color: ${theme.variants.button.primary.disabled
              .backgroundColor};
            border: none;
            cursor: not-allowed;
          }
        `
      : css`
          background-color: transparent;
          border: 1px solid transparent;
        `};

  ${({ disabled }) =>
    disabled &&
    css`
      pointer-events: none;
      opacity: 0.7;
    `}
`

export const Spinner = styled.span`
  width: 16px;
  height: 16px;
  border: 2px solid transparent;
  border-top-color: currentColor;
  border-radius: 50%;
  animation: ${spin} 0.6s linear infinite;
  margin-right: 8px;
`
