import type { Meta, StoryObj } from '@storybook/react';
import ImgBox from './ImgBox';

const meta: Meta<typeof ImgBox> = {
  title: 'common/ImgBox',
  component: ImgBox,
};

export default meta;
type Story = StoryObj<typeof ImgBox>;

// const ChildrenComponent = () => {
//   return (
//     <img
//       src="https://fujifilm-x.com/wp-content/uploads/2021/01/gfx100s_sample_04_thum-1.jpg"
//       alt="Example"
//       style={{
//         objectFit: 'cover',
//         width: '100%',
//         height: '100%',
//         borderRadius: '10px',
//         border: '2px solid #ccc',
//       }}
//     />
//   );
// };

export const Primary: Story = {
  args: {
    width: 100,
    height: 100,
    src: 'https://fujifilm-x.com/wp-content/uploads/2021/01/gfx100s_sample_04_thum-1.jpg',
  },
};
