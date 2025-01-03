import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testapp.R
import com.example.testapp.databinding.ItemProductGalleryBinding
import com.example.testapp.databinding.ItemProductGridBinding
import com.example.testapp.databinding.ItemProductListBinding
import com.example.testapp.domain.model.announcement.AnnouncementItemModel
import com.example.testapp.presentation.searching.viewModel.FragmentUniversalListVM

class UniversalListAdapter(
    private val layoutInflater: LayoutInflater,
    private var itemRepresentationStyle: FragmentUniversalListVM.ItemRepresentationStyle,
    private val onItemClicked: (itemId:Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val announcementList: MutableList<AnnouncementItemModel> = mutableListOf()
    private var isFavorite = false
    companion object {
        private const val VIEW_TYPE_GALLERY = 1
        private const val VIEW_TYPE_MOSAIC = 2
        private const val VIEW_TYPE_LIST = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_GALLERY -> {
                val binding = ItemProductGalleryBinding.inflate(layoutInflater, parent, false)
                GalleryViewHolder(binding)
            }
            VIEW_TYPE_MOSAIC -> {
                val binding = ItemProductGridBinding.inflate(layoutInflater, parent, false) // Replace with mosaic layout
                MosaicViewHolder(binding)
            }
            VIEW_TYPE_LIST -> {
                val binding = ItemProductListBinding.inflate(layoutInflater, parent, false) // Replace with list layout
                ListViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (itemRepresentationStyle) {
            FragmentUniversalListVM.ItemRepresentationStyle.styleGallery -> VIEW_TYPE_GALLERY
            FragmentUniversalListVM.ItemRepresentationStyle.styleMosaic -> VIEW_TYPE_MOSAIC
            FragmentUniversalListVM.ItemRepresentationStyle.styleList -> VIEW_TYPE_LIST
        }
    }

    override fun getItemCount(): Int = announcementList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val announcementItem = announcementList[position]
        when (holder) {
            is GalleryViewHolder -> holder.bind(announcementItem)
            is MosaicViewHolder -> holder.bind(announcementItem)
            is ListViewHolder -> holder.bind(announcementItem)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateStyle(newStyle: FragmentUniversalListVM.ItemRepresentationStyle) {
        itemRepresentationStyle = newStyle
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAnnouncements(newAnnouncements: List<AnnouncementItemModel>) {
        announcementList.clear()
        announcementList.addAll(newAnnouncements)
        notifyDataSetChanged()
    }

    // ViewHolders for different styles

    inner class GalleryViewHolder(
        private val binding: ItemProductGalleryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(announcementItem: AnnouncementItemModel) {
            binding.title.text = announcementItem.title
            binding.price.text = announcementItem.price
            Glide.with(binding.root).load(announcementItem.img_m).into(binding.img)
            binding.starButton.setOnClickListener {
                val item = announcementList[adapterPosition]
                //onItemClicked(item.id)
                if(isFavorite) binding.starButton.setImageResource(R.drawable.vicon_favorite_active)
                else binding.starButton.setImageResource(R.drawable.vicon_favorite_inactive)
                isFavorite = !isFavorite
            }
            binding.baseLay.setOnClickListener {
                val item = announcementList[adapterPosition]
                onItemClicked(item.id)
            }
        }

    }

    inner class MosaicViewHolder(
        private val binding: ItemProductGridBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(announcementItem: AnnouncementItemModel) {
            binding.title.text = announcementItem.title
            binding.price.text = announcementItem.price
            Glide.with(binding.root).load(announcementItem.img_m).into(binding.img)
            // Add specific mosaic layout binding logic if required
            binding.starButton.setOnClickListener {
                val item = announcementList[adapterPosition]
                //onItemClicked(item.id)
                if(isFavorite) binding.starButton.setImageResource(R.drawable.vicon_favorite_active)
                else binding.starButton.setImageResource(R.drawable.vicon_favorite_inactive)
                isFavorite = !isFavorite
            }
            binding.baseLay.setOnClickListener {
                val item = announcementList[adapterPosition]
                onItemClicked(item.id)
            }
        }
    }

    inner class ListViewHolder(
        private val binding: ItemProductListBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(announcementItem: AnnouncementItemModel) {
            binding.title.text = announcementItem.title
            binding.price.text = announcementItem.price
            Glide.with(binding.root).load(announcementItem.img_m).into(binding.img)
            // Add specific list layout binding logic if required
            binding.starButton.setOnClickListener {
                val item = announcementList[adapterPosition]
                //onItemClicked(item.id)
                if(isFavorite) binding.starButton.setImageResource(R.drawable.vicon_favorite_active)
                else binding.starButton.setImageResource(R.drawable.vicon_favorite_inactive)
                isFavorite = !isFavorite
            }
            binding.baseLay.setOnClickListener {
                val item = announcementList[adapterPosition]
                onItemClicked(item.id)
            }
        }
    }
}
