package uz.abbosbek.chatappcodial.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.abbosbek.chatappcodial.databinding.RvItemBinding
import uz.abbosbek.chatappcodial.models.Group

class GroupAdapter(val list: ArrayList<Group>, val function: (Group, Int) -> Unit) :
    RecyclerView.Adapter<GroupAdapter.Vh>() {

    inner class Vh(private val itemRvBinding: RvItemBinding) :
        RecyclerView.ViewHolder(itemRvBinding.root) {
        fun onBind(group: Group, position: Int) {
            itemRvBinding.tvName.text = group.name
           // Glide.with(itemView.context).load(user.imageLink).into(itemRvBinding.image)
itemRvBinding.itemCard.setOnClickListener {
    function(group,position)
}

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) =
        holder.onBind(list[position], position)


    override fun getItemCount(): Int = list.size


}