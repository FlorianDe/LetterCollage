// forward declarations

__kernel void intersectionKernel(__global char *letter_data,
                         __global char *roi_data,
                         __global float *calc_result,
                         const long2 work_size,
                         const int4 kernel_args,
                         const int4 sampler_args,
                         const int2 letter_center,
                         const int2 image_center) {
    int img_width = kernel_args[0];
    int img_height = kernel_args[1];
    int let_width = kernel_args[2];
    int let_height = kernel_args[3];

    int iteration_size = sampler_args[0];
    int letter_hit = sampler_args[1];
    int img_hit = sampler_args[2];

    int x_offset = get_global_id(0); // - (img_width / 2);
    int y_offset = get_global_id(1); // - (img_height / 2);

    int iteration_offset = get_global_id(0) % iteration_size;
    int img_len = img_width * img_height;

    // iteration_size
    for(int i = 0; i < iteration_size; i++) {
        int hits = 0;
        int calc_index = (get_global_id(0) + (get_global_id(1) * img_width)) + img_width*img_height*i;
        for(int x = 0; x < img_width; x++) {
            for(int y = 0; y < img_height; y++) {
                float scale = 1.0 / (float)iteration_size;

                int ind = (x_offset+x)*scale + ((y_offset+y)* scale * img_width);
                if(ind >= 0 && ind < img_len) {
                    char let_pix = letter_data[x + (y*let_width)] * roi_data[ind];
                    if(let_pix > 0) {
                        hits++;
                    }
                }
            }
        }
        calc_result[calc_index] = hits;
    }


}