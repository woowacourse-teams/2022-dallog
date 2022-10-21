import { css, Theme } from '@emotion/react';

const categoryItem = ({ colors, flex }: Theme) => css`
  ${flex.row}

  justify-content: space-around;
  position: relative;

  height: 20rem;
  border-bottom: 1px solid ${colors.GRAY_400};

  font-size: 4rem;

  &:hover {
    background: ${colors.GRAY_100};

    cursor: pointer;
  }
`;

const item = css`
  flex: 1 1 0;

  text-align: center;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
`;

const unsubscribeButton = ({ colors }: Theme) => css`
  position: relative;

  width: 18rem;
  height: 8rem;
  border-radius: 7px;

  background-color: ${colors.GRAY_500};

  font-size: 3.5rem;
  font-weight: 700;
  line-height: 3.5rem;
  color: ${colors.WHITE};

  &:hover {
    filter: none;
  }
`;

const detailStyle = ({ colors }: Theme, hoveringUpside: boolean) => css`
  position: absolute;
  top: ${hoveringUpside && '120%'};
  bottom: ${!hoveringUpside && '120%'};
  z-index: 10;

  width: max-content;
  padding: 4rem 6rem;
  border-radius: 7px;
  box-shadow: 0 2px 5px ${colors.GRAY_500};

  background: ${colors.BLUE_500};

  color: ${colors.WHITE};
`;

export { categoryItem, detailStyle, item, unsubscribeButton };
