import { MdOutlineRefresh } from 'react-icons/md'
import QRCode from 'react-qr-code'
import { QRCodeContainer, Refresh, RefreshQRCode } from './styles'
import Skeleton from 'react-loading-skeleton'

interface QRCodeDisplayProps {
  qrCode?: string | null
  onRefresh: () => void
  isLoading?: boolean
}

export const QRCodeDisplay = ({
  qrCode,
  onRefresh,
  isLoading,
}: QRCodeDisplayProps) => (
  <QRCodeContainer>
    {isLoading ? (
      <Skeleton width={316} height={316} />
    ) : (
      <QRCode value={qrCode || ''} size={316} />
    )}
    {!qrCode && !isLoading && (
      <RefreshQRCode>
        <Refresh
          text="Recarregar"
          leftIcon={MdOutlineRefresh}
          iconSize={32}
          onClick={onRefresh}
          variant="primary"
        />
      </RefreshQRCode>
    )}
  </QRCodeContainer>
)
