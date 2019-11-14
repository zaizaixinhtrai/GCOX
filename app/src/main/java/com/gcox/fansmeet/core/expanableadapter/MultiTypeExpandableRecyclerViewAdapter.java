package com.gcox.fansmeet.core.expanableadapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by thanhbc on 12/22/17.
 */

public abstract class MultiTypeExpandableRecyclerViewAdapter<PVH extends ParentViewHolder, CVH extends ChildViewHolder>
        extends ExpandableRecyclerAdapter<PVH, CVH> {
    /**
     * Primary constructor. Sets up {@link #mParentItemList} and {@link #mItemList}.
     * <p>
     * Changes to {@link #mParentItemList} should be made through add/remove methods in
     * {@link ExpandableRecyclerAdapter}
     *
     * @param parentItemList List of all {@link ParentListItem} objects to be
     *                       displayed in the RecyclerView that this
     *                       adapter is linked to
     */
    public MultiTypeExpandableRecyclerViewAdapter(List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isGroup(viewType)) {
            PVH pvh = onCreateParentViewHolder(parent, viewType);
            pvh.setParentListItemExpandCollapseListener(this);
            return pvh;
        } else if (isChild(viewType)) {
            CVH cvh = onCreateChildViewHolder(parent, viewType);
            return cvh;
        }
        throw new IllegalArgumentException("viewType is not valid");
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object listItem = getListItem(position);
        if (isGroup(getItemViewType(position))) {
            PVH parentViewHolder = (PVH) holder;

            if (parentViewHolder.shouldItemViewClickToggleExpansion()) {
                parentViewHolder.setMainItemClickToExpand();
            }

            ParentWrapper parentWrapper = (ParentWrapper) listItem;
            parentViewHolder.setExpanded(parentWrapper.isExpanded());
            onBindParentViewHolder(parentViewHolder, position, parentWrapper.getParentListItem());
        } else if (listItem == null) {
            throw new IllegalStateException("Incorrect ViewHolder found");
        } else {
            onBindChildViewHolder((CVH) holder, position, listItem);
        }
    }

    @Override
    public PVH onCreateParentViewHolder(ViewGroup parentViewGroup) {
        return null;
    }

    @Override
    public CVH onCreateChildViewHolder(ViewGroup childViewGroup) {
        return null;
    }
    /**
     * Called from {@link #onCreateViewHolder(ViewGroup, int)} when  the list item created is a group
     *
     * @param viewType an int returned by {@link ExpandableRecyclerAdapter#getItemViewType(int)}
     * @param parent the {@link ViewGroup} in the list for which a {@link PVH}  is being created
     * @return A {@link PVH} corresponding to the group list item with the  {@code ViewGroup} parent
     */
    public abstract PVH onCreateParentViewHolder(ViewGroup parent, int viewType);

    /**
     * Called from {@link #onCreateViewHolder(ViewGroup, int)} when the list item created is a child
     *
     * @param viewType an int returned by {@link ExpandableRecyclerAdapter#getItemViewType(int)}
     * @param parent the {@link ViewGroup} in the list for which a {@link CVH}  is being created
     * @return A {@link CVH} corresponding to child list item with the  {@code ViewGroup} parent
     */
    public abstract CVH onCreateChildViewHolder(ViewGroup parent, int viewType);

    /**
     * @param viewType the int corresponding to the viewType of a {@code ExpandableGroup}
     * @return if a subclasses has *NOT* overridden {@code getGroupViewType} than the viewType for
     * the group is defaulted to {@link ExpandableRecyclerAdapter#TYPE_PARENT}
     */
    public abstract boolean isGroup(int viewType);

    /**
     * @param viewType the int corresponding to the viewType of a child of a {@code ExpandableGroup}
     * @return if a subclasses has *NOT* overridden {@code getChildViewType} than the viewType for
     * the child is defaulted to {@link ExpandableRecyclerAdapter#TYPE_CHILD}
     */
    public abstract boolean isChild(int viewType);

    @Override
    public int getItemViewType(int position) {
        Object listItem = getListItem(position);
        if(listItem==null) throw new IllegalStateException("Null object added");
        return getItemViewType(listItem);
    }

    public abstract int getItemViewType(Object item);
}
